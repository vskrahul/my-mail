package com.github.vskrahul.azure.graph.api;

import com.github.vskrahul.azure.graph.model.MailFolders;
import com.github.vskrahul.azure.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Test {
    public static void main(String[] args) {
//        List<MailFolder> folders = new ArrayList<>();
//        folders.add(new MailFolder(100, "A"));
//        folders.add(new MailFolder(200, "custom"));
//        List<MailFolder> accumulator = new ArrayList<>();
//        process(folders, accumulator);
//        accumulator.forEach(mailFolder -> {
//            System.out.println(mailFolder.name);
//        });


        String s = "{\"@odata.context\":\"https://graph.microsoft.com/v1.0/$metadata#users('vsk.rahul%40hotmail.com')/mailFolders\",\"value\":[{\"id\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAU8AAAA=\",\"displayName\":\"Archive\",\"parentFolderId\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQgAAAA=\",\"childFolderCount\":0,\"unreadItemCount\":0,\"totalItemCount\":2,\"sizeInBytes\":209141,\"isHidden\":false},{\"id\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAUYAAAA=\",\"displayName\":\"Conversation History\",\"parentFolderId\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQgAAAA=\",\"childFolderCount\":1,\"unreadItemCount\":0,\"totalItemCount\":0,\"sizeInBytes\":0,\"isHidden\":false},{\"id\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQoAAAA=\",\"displayName\":\"Deleted Items\",\"parentFolderId\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQgAAAA=\",\"childFolderCount\":0,\"unreadItemCount\":0,\"totalItemCount\":0,\"sizeInBytes\":0,\"isHidden\":false},{\"id\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQ8AAAA=\",\"displayName\":\"Drafts\",\"parentFolderId\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQgAAAA=\",\"childFolderCount\":0,\"unreadItemCount\":0,\"totalItemCount\":0,\"sizeInBytes\":0,\"isHidden\":false},{\"id\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQwAAAA=\",\"displayName\":\"Inbox\",\"parentFolderId\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQgAAAA=\",\"childFolderCount\":0,\"unreadItemCount\":12,\"totalItemCount\":24,\"sizeInBytes\":2380750,\"isHidden\":false},{\"id\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAR0AAAA=\",\"displayName\":\"Junk Email\",\"parentFolderId\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQgAAAA=\",\"childFolderCount\":0,\"unreadItemCount\":0,\"totalItemCount\":0,\"sizeInBytes\":0,\"isHidden\":false},{\"id\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAYqObaGAAAA\",\"displayName\":\"MyFolder\",\"parentFolderId\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQgAAAA=\",\"childFolderCount\":1,\"unreadItemCount\":1,\"totalItemCount\":1,\"sizeInBytes\":62114,\"isHidden\":false},{\"id\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQsAAAA=\",\"displayName\":\"Outbox\",\"parentFolderId\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQgAAAA=\",\"childFolderCount\":0,\"unreadItemCount\":0,\"totalItemCount\":0,\"sizeInBytes\":0,\"isHidden\":false},{\"id\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAAANXI68QAAAA==\",\"displayName\":\"RSS Feeds\",\"parentFolderId\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQgAAAA=\",\"childFolderCount\":0,\"unreadItemCount\":0,\"totalItemCount\":0,\"sizeInBytes\":0,\"isHidden\":false},{\"id\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQkAAAA=\",\"displayName\":\"Sent Items\",\"parentFolderId\":\"AQMkADAwATZiZmYAZC0wMAAzOC02ZTg4AC0wMAItMDAKAC4AAAORCScleHlnQ4_9d6r1RvyNAQCxM8wXFs8KSonK8T9D4Yt0AAACAQgAAAA=\",\"childFolderCount\":0,\"unreadItemCount\":0,\"totalItemCount\":6,\"sizeInBytes\":6703862,\"isHidden\":false}],\"@odata.nextLink\":\"https://graph.microsoft.com/v1.0/me/mailFolders?%24skip=10\"}";
        MailFolders mailFolders = JsonUtil.jsonStringToInstance(s, MailFolders.class);
        System.out.println(mailFolders);
    }

    static void process(List<MailFolder> folder, List<MailFolder> accumulator) {
        for(MailFolder v: folder) {
            accumulator.add(v);
            if(hasChild(v)) {
                List<MailFolder> child = child(v);
                process(child, accumulator);
            }
        }
    }

    static List<MailFolder> child(MailFolder mailFolder) {
        List<MailFolder> list = new ArrayList<>();
        switch (mailFolder.id) {
            case 100: //A
                list.add(new MailFolder(101, mailFolder.name + "/B1"));
                list.add(new MailFolder(102, mailFolder.name + "/B2"));
                break;
            case 101: //B1
                list.add(new MailFolder(103, mailFolder.name + "/C1"));
                break;
            case 102: //B2
                list.add(new MailFolder(104, mailFolder.name + "/C2"));
                break;
        }
        return list;
    }

    static boolean hasChild(MailFolder mailFolder) {
        return switch (mailFolder.id) {
            case 100, 101, 102 -> true;
            default -> false;
        };
    }

    static class MailFolder {
        int id;
        String name;
        MailFolder(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
