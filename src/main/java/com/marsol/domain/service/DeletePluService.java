package com.marsol.domain.service;

import com.marsol.infrastructure.integration.SyncDataDownloader;
import com.marsol.infrastructure.repository.PluDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeletePluService {

    @Value("${directory.pendings}")
    private String directoryPendings;

    @Value("${directory_backup}")
    private String directoryBackup;

    private final SyncDataDownloader syncDataDownloader;
    private final PluDAO pluDAO;
    private final PluTransformationService pluTransformationService;

    @Autowired
    public DeletePluService(PluDAO pluDAO,PluTransformationService pluTransformationService) {
        this.syncDataDownloader = new SyncDataDownloader();
        this.pluDAO = pluDAO;
        this.pluTransformationService = pluTransformationService;
    }



}
