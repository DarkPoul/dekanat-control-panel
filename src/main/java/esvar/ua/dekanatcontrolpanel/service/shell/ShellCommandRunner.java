package esvar.ua.dekanatcontrolpanel.service.shell;

import esvar.ua.dekanatcontrolpanel.config.TestEnvironmentProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ShellCommandRunner {

    private static final int MAX_LOG_LENGTH = 64 * 1024;

    private final Set<String> allowedScripts;

    public ShellCommandRunner(TestEnvironmentProperties properties) {
        this.allowedScripts = new HashSet<>(properties.getAllowedScripts());
    }

    public ShellCommandResult execute(List<String> command) {
        if (CollectionUtils.isEmpty(command)) {
            throw new IllegalArgumentException("Command must not be empty");
        }

        ensureAllowed(command);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            String log = readProcessOutput(process.getInputStream());
            int exitCode = process.waitFor();
            return new ShellCommandResult(exitCode, log);
        } catch (IOException e) {
            throw new ShellCommandExecutionException("Failed to execute command", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ShellCommandExecutionException("Command execution was interrupted", e);
        }
    }

    private void ensureAllowed(List<String> command) {
        boolean allowed = command.stream()
                .filter(token -> token != null)
                .anyMatch(token -> allowedScripts.contains(token.trim()));
        if (!allowed) {
            throw new IllegalArgumentException("Execution of the provided command is not allowed");
        }
    }

    private String readProcessOutput(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int remaining = MAX_LOG_LENGTH;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            int read;
            while ((read = reader.read(buffer)) != -1) {
                if (remaining > 0) {
                    int toAppend = Math.min(read, remaining);
                    builder.append(buffer, 0, toAppend);
                    remaining -= toAppend;
                }
            }
        }
        return builder.toString();
    }
}
