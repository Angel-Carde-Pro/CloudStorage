package com.mycompany.cloudstorage;

import com.google.cloud.storage.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet(name = "upload", value = "/upload")
@MultipartConfig()

public class UploadServlet extends HttpServlet {

    private static String BUCKET_NAME = "proyectstorege";
    private static Storage storage = null;

    @Override
    public void init() {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
            final Part filePart = req.getPart("file");
            final String fileName = filePart.getSubmittedFileName();

            List<Acl> acls = new ArrayList<>();
            acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

            Blob blob = storage.create(
                    BlobInfo.newBuilder(BUCKET_NAME, fileName).setAcl(acls).build(),
                    filePart.getInputStream());

            resp.getWriter().print(blob.getMediaLink());
    }
}
