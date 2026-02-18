package com.githubreader.service.impl;

import com.githubreader.dto.ApiResponse;
import com.githubreader.entity.Contributor;
import com.githubreader.entity.Topic;
import com.githubreader.exceptions.ResourceNotFoundException;
import com.githubreader.repository.ContributorRepository;
import com.githubreader.repository.TopicRepository;
import com.githubreader.service.GithubRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class GithubRepoServiceImpl implements GithubRepoService {
    private final ContributorRepository contributorRepository;
    private final TopicRepository topicRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${github.repo-owner}")
    private String repoOwner;

    @Value("${github.repo-name}")
    private String repoName;

    @Value("${github.access-token}")
    private String accessToken;

    public GithubRepoServiceImpl(ContributorRepository contributorRepository, TopicRepository topicRepository) {
        this.contributorRepository = contributorRepository;
        this.topicRepository = topicRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateGitHubData() {
        fetchContributors();
        fetchTopics();
    }



    @Override
    public ApiResponse<List<Contributor>> getContributors() {
        List<Contributor> contributors = contributorRepository.findAll();
        ApiResponse<List<Contributor>> apiResponse = new ApiResponse<>(
                HttpStatus.OK.toString(),
                200,
                "Contributors info",
                false,
                contributors
        );
        return apiResponse;
    }

    @Override
    public ApiResponse<List<Topic>> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        ApiResponse<List<Topic>> apiResponse = new ApiResponse<>(
                HttpStatus.OK.toString(),
                200,
                "Topic info",
                false,
                topics
        );
        return apiResponse;
    }

    @Override
    public ApiResponse<Topic> getTopicBlog(String topicName, String subTopicName) {
        Topic topic = topicRepository.findByNameAndSubTopic(topicName, subTopicName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Topic '" + topicName + "' with subtopic '" + subTopicName + "' not found"));

        ApiResponse<Topic> apiResponse = new ApiResponse<>(
                HttpStatus.OK.toString(),
                200,
                "Topic and sub topic info",
                false,
                topic
        );
        return apiResponse;
    }


    private void fetchTopics() {
        String url = "https://api.github.com/repos/" + repoOwner + "/" + repoName + "/contents/README.md";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        try {
            JsonNode node = objectMapper.readTree(response.getBody());
            String content = node.get("content").asText();
            byte[] decoded = java.util.Base64.getDecoder().decode(content);
            String readmeText = new String(decoded);

            topicRepository.deleteAll();

            for (String line : readmeText.split("\n")) {
                if (line.startsWith("##")) {
                    String[] parts = line.replace("##", "").trim().split("-");
                    if (parts.length == 3) {
                        Topic t = new Topic();
                        t.setName(parts[0].trim());
                        t.setSubTopic(parts[1].trim());
                        t.setBlogUrl(parts[2].trim());
                        topicRepository.save(t);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchContributors() {
        String url = "https://api.github.com/repos/" + repoOwner + "/" + repoName + "/contributors";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        try {
            JsonNode nodes = objectMapper.readTree(response.getBody());
            contributorRepository.deleteAll();
            for (JsonNode node : nodes) {
                Contributor c = new Contributor();
                c.setLogin(node.get("login").asText());
                c.setContributions(node.get("contributions").asInt());
                contributorRepository.save(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
