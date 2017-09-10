package controllers;

public class ApplicationController {
    public String redirectDocs() {
        System.out.println("/swagger.json");
        return "redirect:/swagger.json";
    }
}
