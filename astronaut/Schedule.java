import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Schedule{

    public static class Task {
        private String description;
        private Date startTime;
        private Date endTime;
        private Priority priority;
        private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        public enum Priority {
            HIGH, MEDIUM, LOW
        }

        public Task(String description, Date startTime, Date endTime, Priority priority) {
            this.description = description;
            this.startTime = startTime;
            this.endTime = endTime;
            this.priority = priority;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }

        public Priority getPriority() {
            return priority;
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        @Override
        public String toString() {
            return String.format("Description: %s\nStart Time: %s\nEnd Time: %s\nPriority: %s",
                    description, sdf.format(startTime), sdf.format(endTime), priority);
        }
    }

    public static class TaskManager {
        private List<Task> tasks = new ArrayList<>();
        private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        public void addTask(String description, String startTime, String endTime, String priority) {
            try {
                Date start = sdf.parse(startTime);
                Date end = sdf.parse(endTime);
                if (start.after(end)) {
                    System.out.println("Start time cannot be after end time.");
                    return;
                }
                Task.Priority taskPriority = Task.Priority.valueOf(priority.toUpperCase());
                Task task = new Task(description, start, end, taskPriority);
                tasks.add(task);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD HH:MM.");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority. Please use High, Medium, or Low.");
            }
        }

        public void removeTask(String description) {
            tasks.removeIf(task -> task.getDescription().equalsIgnoreCase(description));
        }

        public void viewTasks() {
            if (tasks.isEmpty()) {
                System.out.println("No tasks available.");
            } else {
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println("Task " + (i + 1) + ":");
                    System.out.println(tasks.get(i));
                    System.out.println("------------------------------");
                }
            }
        }

        public void updateTask(String description, String newDescription, String newStartTime, String newEndTime, String newPriority) {
            for (Task task : tasks) {
                if (task.getDescription().equalsIgnoreCase(description)) {
                    if (!newDescription.isEmpty()) {
                        task.setDescription(newDescription);
                    }
                    if (!newStartTime.isEmpty()) {
                        try {
                            Date newStart = sdf.parse(newStartTime);
                            if (newStart.after(task.getEndTime())) {
                                System.out.println("New start time cannot be after end time.");
                                return;
                            }
                            task.setStartTime(newStart);
                        } catch (ParseException e) {
                            System.out.println("Invalid date format for start time.");
                        }
                    }
                    if (!newEndTime.isEmpty()) {
                        try {
                            Date newEnd = sdf.parse(newEndTime);
                            if (newEnd.before(task.getStartTime())) {
                                System.out.println("New end time cannot be before start time.");
                                return;
                            }
                            task.setEndTime(newEnd);
                        } catch (ParseException e) {
                            System.out.println("Invalid date format for end time.");
                        }
                    }
                    if (!newPriority.isEmpty()) {
                        try {
                            Task.Priority newTaskPriority = Task.Priority.valueOf(newPriority.toUpperCase());
                            task.setPriority(newTaskPriority);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid priority. Please use High, Medium, or Low.");
                        }
                    }
                    return;
                }
            }
            System.out.println("Task not found.");
        }
    }

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Astronaut Schedule Manager ---");
            System.out.println("1. Add Task");
            System.out.println("2. Remove Task");
            System.out.println("3. View Tasks");
            System.out.println("4. Update Task");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter start time (YYYY-MM-DD HH:MM): ");
                    String startTime = scanner.nextLine();
                    System.out.print("Enter end time (YYYY-MM-DD HH:MM): ");
                    String endTime = scanner.nextLine();
                    System.out.print("Enter priority (High/Medium/Low): ");
                    String priority = scanner.nextLine();
                    manager.addTask(description, startTime, endTime, priority);
                    break;

                case "2":
                    System.out.print("Enter task description to remove: ");
                    String removeDescription = scanner.nextLine();
                    manager.removeTask(removeDescription);
                    break;

                case "3":
                    manager.viewTasks();
                    break;

                case "4":
                    System.out.print("Enter task description to update: ");
                    String updateDescription = scanner.nextLine();
                    System.out.print("Enter new description (leave blank for no change): ");
                    String newDescription = scanner.nextLine();
                    System.out.print("Enter new start time (YYYY-MM-DD HH:MM, leave blank for no change): ");
                    String newStartTime = scanner.nextLine();
                    System.out.print("Enter new end time (YYYY-MM-DD HH:MM, leave blank for no change): ");
                    String newEndTime = scanner.nextLine();
                    System.out.print("Enter new priority (High/Medium/Low, leave blank for no change): ");
                    String newPriority = scanner.nextLine();
                    manager.updateTask(updateDescription, newDescription, newStartTime, newEndTime, newPriority);
                    break;

                case "5":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }
}
