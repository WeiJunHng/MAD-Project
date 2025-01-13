package com.example.madproject.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.madproject.data.DAO.DiscussionCommentDAO;
import com.example.madproject.data.DAO.DiscussionDAO;
import com.example.madproject.data.DAO.DiscussionLikeDAO;
import com.example.madproject.data.db.AppDatabase;
import com.example.madproject.data.db.FirestoreManager;
import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.DiscussionComment;
import com.example.madproject.data.model.DiscussionLike;
import com.example.madproject.data.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DiscussionRepository {

    private final Context context;
    private final FirestoreManager firestoreManager;
    private final DiscussionDAO discussionDAO;
    private final DiscussionCommentDAO discussionCommentDAO;
    private final DiscussionLikeDAO discussionLikeDAO;
    private final UserRepository userRepository;
    private final HashMap<String, Discussion> discussionMap = new HashMap<>();

    public DiscussionRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.context = context;
        firestoreManager = new FirestoreManager(database);
        discussionDAO = database.discussionDAO();
        discussionCommentDAO = database.discussionCommentDAO();
        discussionLikeDAO = database.discussionLikeDAO();
        userRepository = new UserRepository(context);
//        fetchDiscussions();
    }

    public String createDiscussionId() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> {
            fetchDiscussions();
            String lastId = discussionDAO.getLastId();
            if (lastId == null) {
                return String.format("D%06d", 1);
            }
            int numId = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("D%06d", numId);
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String createDiscussionCommentId() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> {
            fetchDiscussions();
            String lastId = discussionCommentDAO.getLastId();
            if (lastId == null) {
                return String.format("C%06d", 1);
            }
            int numId = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("C%06d", numId);
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Discussion> getAllDiscussion() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Discussion>> future = executorService.submit(() -> {
            firestoreManager.clearDiscussionTables();
            fetchDiscussions();

            Log.d("Discussion", "Fetched discussion");
            // Sort from latest to oldest
            List<Discussion> discussions = discussionDAO.getAll();
            discussions.sort((discussion1, discussion2) -> discussion2.getTimestamp().compareTo(discussion1.getTimestamp()));
            return discussions;
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DiscussionComment> getAllCommentByDiscussion(String discussionId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<DiscussionComment>> future = executorService.submit(() -> {
            firestoreManager.clearDiscussionTables();
            fetchDiscussions();
            return discussionCommentDAO.getAllByDiscussionId(discussionId);
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCommentCount(Discussion discussion) {
        return getAllCommentByDiscussion(discussion.getId()).size();
    }

    public int getLikeCount(Discussion discussion) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(() -> {
            fetchDiscussions();
            return discussionLikeDAO.getDiscussionLikeCount(discussion.getId());
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isLiked(Discussion discussion, User user) {
        return discussionLikeDAO.isLiked(discussion.getId(), user.getId());
    }

    public void fetchDiscussions() {
        firestoreManager.syncDiscussionTable();
    }

    public void insertDiscussionToFirestore(Discussion discussion) {
        firestoreManager.executeAction(FirestoreManager.Action.INSERT, "discussion", discussion, context);
    }

    public void insertDiscussionCommentInFirestore(DiscussionComment discussionComment) {
        firestoreManager.executeAction(FirestoreManager.Action.INSERT, "discussionComment", discussionComment, context);
    }

    public DiscussionLike getDiscussionLike(String discussionId, String userId) {
        return discussionLikeDAO.getDiscussionLike(discussionId, userId);
    }

    public void insertDiscussionLikeInFirestore(DiscussionLike discussionLike) {
        firestoreManager.executeAction(FirestoreManager.Action.INSERT, "discussionLike", discussionLike, context);
    }

    public void deleteDiscussionLikeInFirestore(DiscussionLike discussionLike) {
        firestoreManager.executeAction(FirestoreManager.Action.DELETE, "discussionLike", discussionLike, context);
    }

    public Discussion getDiscussionById(String id) {
        if (discussionMap.containsKey(id)) {
            return discussionMap.get(id);
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Discussion> future = executorService.submit(() -> {
            fetchDiscussions();
            Discussion discussion = discussionDAO.getById(id);
            discussionMap.put(id, discussion);
            return discussion;
        });

//        Discussion discussion = null;
        try {
//            discussion = future.get();
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public User getAuthor(Discussion discussion) {
        return userRepository.getUserById(discussion.getAuthorId());
    }

    public String getAuthorUsername(Discussion discussion) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> {
            User author = getAuthor(discussion);
            if(author == null) return null;
            return author.getFormattedUsername();
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
//            return null;
            throw new RuntimeException(e);
        }
    }

}
