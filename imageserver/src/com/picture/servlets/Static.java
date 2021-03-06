package com.picture.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

@WebServlet("/static/*")
public class    Static extends HttpServlet {

    private String imageLibraryPath = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        imageLibraryPath = this.getServletContext().getInitParameter("rep_images");

        String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
        File file = new File(imageLibraryPath, filename);

        if (!file.exists()) file = new File(imageLibraryPath, "default.png");

        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

        Files.copy(file.toPath(), response.getOutputStream());
    }
}