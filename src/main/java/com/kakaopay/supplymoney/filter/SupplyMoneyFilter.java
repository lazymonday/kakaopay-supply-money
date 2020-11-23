package com.kakaopay.supplymoney.filter;

import com.kakaopay.supplymoney.constants.Header;
import com.kakaopay.supplymoney.constants.SupplyMoneyStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter
@Component
public class SupplyMoneyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        String userId = servletRequest.getHeader(Header.USER_ID);
        String roomId = servletRequest.getHeader(Header.ROOM_ID);

        if (!validateHeader(userId, roomId)) {
            sendResponse((HttpServletResponse) response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendResponse(HttpServletResponse response) throws IOException {
        String body = "{\"errCode\": \"%s\", \"errMsg\": \"%s\", \"result\": null}";
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(String.format(body,
                SupplyMoneyStatus.BAD_REQUEST.getCode(),
                SupplyMoneyStatus.BAD_REQUEST.getDesc()));
        writer.flush();
    }

    private boolean validateHeader(String userId, String roomId) {
        boolean validHeader = true;
        if (userId == null || roomId == null) {
            validHeader = false;
        } else {
            try {
                Long.parseLong(userId);
            } catch (NumberFormatException ex) {
                validHeader = false;
            }
        }

        return validHeader;
    }
}
