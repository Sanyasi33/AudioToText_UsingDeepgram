package com.dr.controller;

import com.dr.service.AudioToTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/audio-to-text")
public class AudioToTextController {

    @Autowired
    private AudioToTextService audioToTextService;

    @RequestMapping
    public String showAudioToTextForm() {
        return "audio-to-text";  // Return the Thymeleaf view for audio-to-text
    }

    @PostMapping("/transcribe")
    public String audioToText(@RequestParam MultipartFile file, Model model) {
        try {
            // Get audio data as byte array
            byte[] audioData = file.getBytes();
            // Transcribe the audio to text
            String text = audioToTextService.transcribeAudio(audioData);

            // Add the transcribed text to the model
            model.addAttribute("text", text);
            return "audio-to-text";  // Return the same view with the transcribed text
        } catch (Exception e) {
            // Handle exceptions and show a default message
            model.addAttribute("text", "Something went wrong. Please try again later.");
            return "audio-to-text";
        }
    }
}
