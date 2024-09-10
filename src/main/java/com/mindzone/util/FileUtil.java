package com.mindzone.util;

import com.mindzone.model.File;

import java.util.List;

public class FileUtil {

    // contains based on id
    public static boolean contains(List<? extends File> list, File element) {
        for (File reportFile : list) {
            String fileId = reportFile.getId();
            if (fileId != null && fileId.equals(element.getId())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasUpdated(File oldFile, File newFile) {
        return !oldFile.getContent().equals(newFile.getContent()) ||
                !oldFile.getName().equals(newFile.getName());
    }
}
