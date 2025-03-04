package com.github.vskrahul.azure.graph.api;

import com.github.vskrahul.azure.graph.model.Folder;
import com.github.vskrahul.azure.graph.model.MailFolders;
import com.github.vskrahul.azure.util.JsonUtil;
import kotlin.collections.ArrayDeque;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.github.vskrahul.azure.graph.OnBehalfOfFlowAzureAuthentication.GRAPH_API_URL;

@Slf4j
public class MailFolderApi {

    public static List<Folder> mailFolders(String accessToken) {
        try {
            URL url = new URL(GRAPH_API_URL + "/mailFolders");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            log.info("[method=mailFolders] response={}", response);

            MailFolders mailFolders = JsonUtil.jsonStringToInstance(response.toString(), MailFolders.class);
            List<Folder> accumulatedMailFolders = new ArrayList<>();
            checkForChildFolders(mailFolders.getValue(), accessToken, accumulatedMailFolders);
            return accumulatedMailFolders;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static void checkForChildFolders(List<Folder> resultedList,
                                                     String accessToken,
                                                     List<Folder> accumulatedMailFolders) {
        for(Folder folder : resultedList) {
            accumulatedMailFolders.add(folder);
            if(folder.getChildFolderCount() > 0) {
                List<Folder> childFolders = childMailFolders(accessToken, folder);
                checkForChildFolders(childFolders, accessToken, accumulatedMailFolders);
            }
        }
    }

    public static List<Folder> childMailFolders(String accessToken, Folder parentFolder) {
        try {
            URL url = new URL(GRAPH_API_URL + "/mailFolders/" + parentFolder.getId() + "/childFolders");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            log.info("[method=childMailFolders] [parentFolderName={}] [value={}]", parentFolder.getDisplayName(), response);

            MailFolders mailFolders = JsonUtil.jsonStringToInstance(response.toString(), MailFolders.class);
            mailFolders.getValue().forEach(v -> {
                v.setDisplayName(parentFolder.getDisplayName() + "/" + v.getDisplayName());
            });
            return mailFolders.getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
