package com.arquitechthor.kopi.sync;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/settings/sync")
public class SyncController {

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("isConfigured", syncService.isConfigured());
        model.addAttribute("bucketName", syncService.getBucketName());
        return "sync";
    }

    @PostMapping("/upload")
    public String upload(RedirectAttributes redirectAttributes) {
        try {
            if (!syncService.isConfigured()) {
                redirectAttributes.addFlashAttribute("error", "AWS not configured or bucket name missing.");
                return "redirect:/settings/sync";
            }
            syncService.upload();
            redirectAttributes.addFlashAttribute("success", "Backup uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/settings/sync";
    }
}
