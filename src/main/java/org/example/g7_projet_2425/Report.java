package org.example.g7_projet_2425;

class Report {
    private int id;
    private String content;
    private String format;

    public Report(int id, String content, String format) {
        this.id = id;
        this.content = content;
        this.format = format;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void generateReport(Project project) {
        this.content = "Report for Project: " + project.getTitle() + "\n\n";
        for (Task task : project.getTasks()) {
            this.content += task.getTitle() + " - Status: " + task.getStatus() + "\n";
        }
    }

    public void exportReport(String format) {
        System.out.println("Exporting report in format: " + format);
        // Simulate exporting logic
    }
}