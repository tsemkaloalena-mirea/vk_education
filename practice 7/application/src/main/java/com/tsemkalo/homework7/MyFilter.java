package com.tsemkalo.homework7;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

public final class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (httpServletRequest.getMethod().equals("GET")) {
            chain.doFilter(request, response);
            return;
        }
        if (httpServletRequest.getMethod().equals("POST")) {
            try {
                Objects.requireNonNull(httpServletRequest.getParameter("productName"));
                Objects.requireNonNull(httpServletRequest.getParameter("amount"));
                Objects.requireNonNull(httpServletRequest.getParameter("manufacturerName"));
            } catch (NullPointerException exception) {
                exception.printStackTrace();
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
                Integer.parseInt(httpServletRequest.getParameter("amount"));
                chain.doFilter(request, response);
                return;
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }
        }
        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public void destroy() {
    }
}