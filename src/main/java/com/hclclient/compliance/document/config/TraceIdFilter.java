package com.hclclient.compliance.document.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TraceIdFilter extends OncePerRequestFilter {
  public static final String TRACE_ID = "traceId";
  public static final String TRACE_HEADER = "X-Trace-Id";
  @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    String traceId = request.getHeader(TRACE_HEADER);
    if (traceId == null || traceId.isBlank()) traceId = UUID.randomUUID().toString();
    try { MDC.put(TRACE_ID, traceId); request.setAttribute(TRACE_ID, traceId); response.setHeader(TRACE_HEADER, traceId); chain.doFilter(request, response); }
    finally { MDC.remove(TRACE_ID); }
  }
}
