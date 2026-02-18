package com.githubreader.service;

import com.githubreader.dto.ApiResponse;
import com.githubreader.entity.Contributor;
import com.githubreader.entity.Topic;

import java.util.List;

public interface GithubRepoService {
    ApiResponse<List<Contributor>> getContributors();

    ApiResponse<List<Topic>> getAllTopics();

    ApiResponse<Topic> getTopicBlog(String topicName, String subTopicName);
}
