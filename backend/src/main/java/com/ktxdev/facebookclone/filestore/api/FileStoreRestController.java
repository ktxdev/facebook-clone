package com.ktxdev.facebookclone.filestore.api;

import com.ktxdev.facebookclone.filestore.service.FileStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class FileStoreRestController {

    private final FileStoreService fileStoreService;

    @GetMapping("opn/v1/filestore/{filename}")
    public byte[] download (
            @PathVariable String filename,
            @RequestParam(required = false, defaultValue = "") String directory
    ) {
        return fileStoreService.download(directory, filename);
    }
}
