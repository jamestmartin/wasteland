package me.jamestmartin.wasteland.manual;

import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManual implements CommandExecutor {
    private final String manualName;
    private final ManualSection manual;
    
    public CommandManual(String manualName, ManualSection manual) {
        this.manualName = manualName;
        this.manual = manual;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 2) {
            sender.sendMessage("Too many arguments.");
            return false;
        }
        
        final String sectionPath;
        final CommandSender target;
        if (args.length == 2) {
            if (!ManualSection.isSectionPath(args[0])) {
                sender.sendMessage("Not a valid section identifier: " + args[0]);
                return false;
            }
            sectionPath = args[0];
            
            target = sender.getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("No such player: " + args[1]);
                return false;
            }
        } else if (args.length == 1) {
            if (ManualSection.isSectionPath(args[0])) {
                sectionPath = args[0];
                target = sender;
            } else {
                sectionPath = "";
                target = sender.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage("No such player: " + args[0]);
                    return false;
                }
            }
        } else {
            sectionPath = "";
            target = sender;
        }
        
        if (target != sender && !sender.hasPermission("wasteland.manual." + manualName + ".show-other")) {
            sender.sendMessage("You do not have permission to forcibly show other players the " + manual.getSummary() + ".");
            return true;
        }
        
        if (target != sender && !target.hasPermission("wasteland.manual." + manualName)) {
            sender.sendMessage("You cannot show someone a manual they do not have permission to view!");
            return true;
        }
        
        final Optional<ManualSection> section = manual.getSection(sectionPath);
        if (section.isEmpty()) {
            sender.sendMessage(manual.getSummary() + " does not contain section " + sectionPath + ".");
            return false;
        }
        
        if (target != sender) {
            target.sendMessage("A moderator would like you to review this section of " + manual.getSummary() + ":");
        }
        
        if (sectionPath.isEmpty()) {
            sendManual(target, section.get());
        } else {
            target.sendMessage(manual.getSummary() + " section " + sectionPath + ":");
            sendManual(target, section.get(), Optional.of(1), 1);
        }
        
        if (!section.get().getSections().isEmpty()) {
            String pathPrefix = sectionPath.isEmpty() ? "" : sectionPath + ".";
            target.sendMessage("For more detail, see `/" + command.getName() + " " + pathPrefix + "#`.");
        }
        
        if (target != sender) {
            if (!sectionPath.isEmpty()) {
                sender.sendMessage(target.getName() + " has been shown " + manualName + " section " + sectionPath + ".");
            }
            sender.sendMessage(target.getName() + " has been shown " + manualName + ".");
        }
        
        return true;
    }
    
    private static void sendManual(CommandSender target, ManualSection section, Optional<Integer> maxDepth, int currentIndentation, Optional<Integer> sectionNo) {
        StringBuilder indentBuilder = new StringBuilder();
        for (int i = 0; i < currentIndentation; i++) {
            indentBuilder.append("   ");
        }
        String indent = indentBuilder.toString();
        
        String header = section.getSummary();
        if (sectionNo.isPresent()) {
            header = sectionNo.get() + ". " + header;
        }
        header = indent + header;
        if (section.getDetails().isPresent() && maxDepth.map(d -> d > 0).orElse(true)) {
            header += " " + section.getDetails().get();
        }
        target.sendMessage(header);
        
        if (maxDepth.map(d -> d > 0).orElse(true)) {
            for (int i = 0; i < section.getSections().size(); i++) {
                sendManual(target, section.getSections().get(i), maxDepth.map(d -> d - 1), currentIndentation + 1, i + 1);
            }
        }
    }
    
    private static void sendManual(CommandSender target, ManualSection section, Optional<Integer> maxDepth, int currentIndentation, int sectionNo) {
        sendManual(target, section, maxDepth, currentIndentation, Optional.of(sectionNo));
    }
    
    private static void sendManual(CommandSender target, ManualSection section, Optional<Integer> maxDepth, int currentIndentation) {
        sendManual(target, section, maxDepth, currentIndentation, Optional.empty());
    }
    
    private static void sendManual(CommandSender target, ManualSection section, Optional<Integer> maxDepth) {
        sendManual(target, section, maxDepth, 0);
    }
    
    private static void sendManual(CommandSender target, ManualSection section, int maxDepth) {
        sendManual(target, section, Optional.of(maxDepth));
    }
    
    private static void sendManual(CommandSender target, ManualSection section) {
        sendManual(target, section, 1);
    }
}
