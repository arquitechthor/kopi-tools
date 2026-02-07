package com.arquitechthor.kopi.links;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @GetMapping
    public String listLinks(Model model) {
        model.addAttribute("groupedLinks", linkService.getLinksGroupedByCategory());
        return "list";
    }

    @GetMapping("/{id}")
    public String viewLink(@PathVariable Long id, Model model) {
        return linkService.getLinkById(id)
                .map(link -> {
                    model.addAttribute("link", link);
                    return "view";
                })
                .orElse("redirect:/links");
    }

    @GetMapping("/new")
    public String newLink(Model model) {
        model.addAttribute("link", new Link());
        return "form";
    }

    @GetMapping("/{id}/edit")
    public String editLink(@PathVariable Long id, Model model) {
        return linkService.getLinkById(id)
                .map(link -> {
                    model.addAttribute("link", link);
                    return "form";
                })
                .orElse("redirect:/links");
    }

    @PostMapping
    public String saveLink(@Valid @ModelAttribute Link link, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "form";
        }
        linkService.saveLink(link);
        return "redirect:/links";
    }

    @GetMapping("/{id}/delete")
    public String deleteLink(@PathVariable Long id) {
        linkService.deleteLink(id);
        return "redirect:/links";
    }
}
