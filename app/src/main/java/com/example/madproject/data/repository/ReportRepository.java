package com.example.madproject.data.repository;

import android.content.Context;

import com.example.madproject.data.DAO.ReportDAO;
import com.example.madproject.data.db.AppDatabase;
import com.example.madproject.data.db.FirestoreManager;
import com.example.madproject.data.model.Report;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReportRepository {

    private final Context context;
    private final FirestoreManager firestoreManager;
    private final ReportDAO reportDAO;

    public ReportRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.context = context;
        firestoreManager = new FirestoreManager(database);
        reportDAO = database.reportDAO();
//        fetchDiscussions();
    }

    public void fetchReports() {
        firestoreManager.syncReportTable();
    }

    public String createReportId() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> {
            fetchReports();
            String lastId = reportDAO.getLastId();
            if (lastId == null) {
                return String.format("R%06d", 1);
            }
            int numId = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("R%06d", numId);
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Report> getAllReport() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Report>> future = executorService.submit(() -> {
//            firestoreManager.clearReportTables();
            fetchReports();

            // Sort from latest to oldest
            List<Report> discussions = reportDAO.getAll();
            discussions.sort((report1, report2) -> report2.getTimestamp().compareTo(report1.getTimestamp()));
            return discussions;
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertReportInFirestore(Report report) {
        firestoreManager.executeAction(FirestoreManager.Action.INSERT, "report", report, context);
    }

    public void deleteReportInFirestore(Report report) {
        firestoreManager.executeAction(FirestoreManager.Action.DELETE, "report", report, context);
    }

}
