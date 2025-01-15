package org.example.g7_projet_2425.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.collections.ObservableList;
import org.example.g7_projet_2425.Task;

import java.time.LocalDate;

public class CalendarController {

    @FXML
    private DatePicker calendarPicker;

    @FXML
    private TextArea calendarTasks;

    private ObservableList<Task> taskList;

    @FXML
    public void initialize() {
        calendarPicker.setOnAction(event -> displayTasksForSelectedDate());
    }

    public void setTaskList(ObservableList<Task> tasks) {
        this.taskList = tasks;
    }

    private void displayTasksForSelectedDate() {
        LocalDate selectedDate = calendarPicker.getValue();
        if (selectedDate != null && taskList != null) {
            StringBuilder tasksForDate = new StringBuilder();
            for (Task task : taskList) {
                if (task.getDeadline().equals(selectedDate)) {
                    tasksForDate.append(task.getTitle()).append("\n");
                }
            }
            calendarTasks.setText(tasksForDate.toString());
        }
    }
}
