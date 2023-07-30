package com.kanezi.clevercloudexample;

import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URL;

@Controller
@RequestMapping("/")
@Value
@Log4j2
public class AvatarController {

    AvatarService avatarService;

    record AvatarForm(
            String name,
            MultipartFile image
    ) {
    }

    @GetMapping
    String showAvatars(Model model, @ModelAttribute AvatarForm avatarForm) {
        log.info("show avatars: {}", avatarForm);
        model.addAttribute("avatars", avatarService.getAvatars());

        return "avatars";
    }


    @PostMapping
    String insertAvatar(AvatarForm avatarForm, RedirectAttributes attributes) {
        log.info("inserting avatar {}", avatarForm);

        attributes.addFlashAttribute("avatarForm", avatarForm);

        try {
            Avatar avatar = avatarService.insertAvatar(avatarForm.name, avatarForm.image);
            attributes.addFlashAttribute("toast", String.format("Inserted avatar: %s!", avatar.name));
            attributes.addFlashAttribute("avatarForm", new AvatarForm("", null));
        } catch (Exception e) {
            attributes.addFlashAttribute("toast", String.format("Error inserting avatar: %s", e.getMessage()));
            log.error(e.getMessage());
        }


        return "redirect:/";
    }

    @DeleteMapping("/{avatar}")
    public String deleteAvatar(@PathVariable String avatar, RedirectAttributes attributes) {

        try {
            avatarService.deleteAvatar(avatar);
            attributes.addFlashAttribute("toast", String.format("Deleted avatar: %s", avatar));
        } catch (Exception e) {
            attributes.addFlashAttribute("toast", String.format("Error deleting avatar %s => %s", avatar, e.getMessage()));
        }

        return "redirect:/";
    }
}
