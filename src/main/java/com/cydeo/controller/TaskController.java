package com.cydeo.controller;

import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;

    public TaskController(TaskService taskService, ProjectService projectService, UserService userService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createTask(Model model){
        model.addAttribute("task", new TaskDTO());
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("employees", userService.findEmployees());
        model.addAttribute("tasks", taskService.findAll());
        return "task/create";
    }

    @PostMapping("/create")
    public String insertTask(TaskDTO task){
        taskService.save(task);
        return "redirect:/task/create";
    }

    @GetMapping("/update/{id}")
    public String editTask(@PathVariable("id") Long taskId, Model model){
        model.addAttribute("task", taskService.findById(taskId));
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("employees", userService.findEmployees());
        model.addAttribute("tasks", taskService.findAll());
        return "/task/update";
    }

    @PostMapping("/update/{id}")
    public String updateTask(TaskDTO task){
        taskService.update(task);
        return "redirect:/task/create";
    }

    @GetMapping("delete/{id}")
    public String deleteTask(@PathVariable("id") Long id){
        taskService.deleteById(id);
        return "redirect:/task/create";
    }

    //Employee part:
        //pending tasks (status - inProgress tasks):
    @GetMapping("/employee/pending-tasks")
    public String employeePendingTasks(Model model){
        model.addAttribute("tasks", taskService.findAllTasksByStatusIsNot(Status.COMPLETE));
        return "/task/pending-tasks";
    }

        //archive tasks (status - completed tasks):
    @GetMapping("/employee/archive")
    public String employeeArchiveTasks(Model model){
        model.addAttribute("tasks", taskService.findAllTasksByStatus(Status.COMPLETE));
        return "/task/archive";
    }

    //update Pending Tasks from inProgress to Complete:
    @GetMapping("/employee/edit/{id}")
    public String employeeEditTask(@PathVariable("id") Long id, Model model){
        model.addAttribute("task", taskService.findById(id));
        model.addAttribute("statuses", Status.values());
        model.addAttribute("tasks", taskService.findAllTasksByStatusIsNot(Status.COMPLETE));
        return "/task/status-update";
    }

    @PostMapping("/employee/update/{id}")
    public String employeeUpdateTask(TaskDTO task){
        taskService.updateTaskStatus(task);
        return "redirect:/task/employee/pending-tasks";
    }





}
