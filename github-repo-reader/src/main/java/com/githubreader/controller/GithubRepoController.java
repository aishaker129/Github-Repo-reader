package com.githubreader.controller;

import com.githubreader.dto.ApiResponse;
import com.githubreader.entity.Contributor;
import com.githubreader.entity.Topic;
import com.githubreader.service.GithubRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequestMapping("/api/v1")
public class GithubRepoController {
    private final GithubRepoService githubRepoService;

    public GithubRepoController(GithubRepoService githubRepoService) {
        this.githubRepoService = githubRepoService;
    }

    @GetMapping("/read_contributions")
    public ResponseEntity<ApiResponse<List<Contributor>>> getContributors() {
        return ResponseEntity.status(HttpStatus.OK).body(githubRepoService.getContributors());
    }

    @GetMapping("/read_indices")
    public ResponseEntity<ApiResponse<List<Topic>>> getAllTopics() {
        return ResponseEntity.status(HttpStatus.OK).body(githubRepoService.getAllTopics());
    }

    @GetMapping("/read_blog")
    public ResponseEntity<ApiResponse<Topic>> getBlog(@RequestParam String topic_name,
                         @RequestParam String sub_topic_name) {
        return ResponseEntity.status(HttpStatus.OK).body(githubRepoService.getTopicBlog(topic_name, sub_topic_name));
    }

}
