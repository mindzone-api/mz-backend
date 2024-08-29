package com.mindzone.service.impl;

import com.mindzone.enums.FileType;
import com.mindzone.model.therapy.File;
import com.mindzone.repository.FileRepository;
import com.mindzone.service.interfaces.FileService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private FileRepository fileRepository;
    private UltimateModelMapper m;

    @Override
    public void save(File model) {
        Date now = new Date();
        if (model.getCreatedAt() == null) {
            model.setCreatedAt(now);
        }
        model.setUpdatedAt(now);
        fileRepository.save(model);
    }

    @Override
    public List<File> updateSessionFiles(String sessionId, List<File> newFiles, FileType fileType) {
        List<File> files = fileRepository.findAllBySessionIdAndFileType(sessionId, fileType);

        // Deleting all files ocurrences that are not on the updated list
        fileRepository.deleteAll(files.stream().filter(
                file -> !newFiles.contains(file)
        ).toList());

        for (File file : newFiles) {
            if (files.contains(file)) { // comparing using only the id
                File fileOnList = files.get(files.indexOf(file)); // It is possible to retrieve the file because of its id
                m.map(file, fileOnList);
                file = fileOnList;
            } else {
                file.setSessionId(sessionId);
                file.setFileType(fileType);
                files.add(file);
            }
            save(file);
        }
        return files;
    }

    @Override
    public List<File> getBySessionIdAndFileType(String sessionId, FileType fileType) {
        return fileRepository.findAllBySessionIdAndFileType(sessionId, fileType);
    }
}
