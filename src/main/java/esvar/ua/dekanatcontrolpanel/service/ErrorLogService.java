package esvar.ua.dekanatcontrolpanel.service;

import esvar.ua.dekanatcontrolpanel.dto.ErrorLogDetailsDto;
import esvar.ua.dekanatcontrolpanel.dto.ErrorLogPageDto;
import esvar.ua.dekanatcontrolpanel.dto.ErrorLogSummaryDto;
import esvar.ua.dekanatcontrolpanel.entity.ErrorLog;
import esvar.ua.dekanatcontrolpanel.repo.ErrorLogRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ErrorLogService {

    private final ErrorLogRepository errorLogRepository;

    public ErrorLogService(ErrorLogRepository errorLogRepository) {
        this.errorLogRepository = errorLogRepository;
    }

    public List<ErrorLogSummaryDto> getRecentLogs(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return errorLogRepository.findAllByOrderByCreatedAtDesc(pageable)
                .getContent()
                .stream()
                .map(ErrorLogSummaryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ErrorLogPageDto getLogs(String env, ErrorLog.Severity severity, Instant from, Instant to, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ErrorLog> result = errorLogRepository.findByFilters(env, severity, from, to, pageable);
        List<ErrorLogSummaryDto> items = result.getContent().stream()
                .map(ErrorLogSummaryDto::fromEntity)
                .collect(Collectors.toList());
        return new ErrorLogPageDto(items, result.getTotalElements(), page, pageSize);
    }

    public Optional<ErrorLogDetailsDto> getLogDetails(Long id) {
        return errorLogRepository.findById(id).map(ErrorLogDetailsDto::fromEntity);
    }
}