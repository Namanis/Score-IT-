package com.scoreit.scoreit.controller.web;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;

final class Redirects {

    private static final String FALLBACK = "/games";

    private Redirects() {
    }

    static String back(HttpServletRequest request) {
        return "redirect:" + backPath(request);
    }

    static String back(HttpServletRequest request, String anchor) {
        return "redirect:" + backPath(request) + "#" + anchor;
    }

    static String backPath(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            return FALLBACK;
        }
        try {
            String path = URI.create(referer).getRawPath();
            if (path == null || path.isBlank() || path.equals(request.getRequestURI())) {
                return FALLBACK;
            }
            return path;
        } catch (IllegalArgumentException e) {
            return FALLBACK;
        }
    }
}
