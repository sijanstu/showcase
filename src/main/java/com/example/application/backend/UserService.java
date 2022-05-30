package com.example.application.backend;

import com.example.application.Application;
import com.example.application.entity.UserModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sijan Bhandari
 */
@Repository
@Service
public class UserService {

    static public boolean checkUserNameAvailability(String username) throws InterruptedException, ExecutionException {
        List<QueryDocumentSnapshot> documents = Application.firestore.collection("traders").whereEqualTo("username", username).get().get().getDocuments();
        System.out.println(documents.size());
        return !documents.isEmpty();
    }

    static public UserModel findByUsername(String username) {
        try {
            List<QueryDocumentSnapshot> documents = Application.firestore.collection("traders").whereEqualTo("username", username).get().get().getDocuments();
            if (!documents.isEmpty()) {
                if (documents.get(0).exists()) {
                    UserModel user = documents.get(0).toObject(UserModel.class);
                    return user;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    static public boolean checkUserAvailability(String username, String password) {
        try {
            DocumentSnapshot documentSnapshot = Application.firestore.collection("traders").whereEqualTo("username", username).whereEqualTo("password", password).get().get().getDocuments().get(0);
            return documentSnapshot.exists();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    static public boolean deleteUser(String uid) {
        ApiFuture<WriteResult> writeResult = Application.firestore.collection("traders").document(uid).delete();
        //System.out.println("Update time : " + writeResult.get().getUpdateTime());
        return true;
    }

    static public boolean checkuserUidAvailability(String uid) {
        try {
            DocumentSnapshot documentSnapshot = Application.firestore.collection("traders").document(uid).get().get();
            return documentSnapshot.exists();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
    }

    static public UserModel addUser(UserModel user) {
        if (user.getUid() != null) {
            updateUser(user);
        } else {
            UUID uniqueKey = UUID.randomUUID();
            user.setUid(String.valueOf(uniqueKey));
            ApiFuture<WriteResult> docRef = Application.firestore.collection("traders").document(String.valueOf(user.getUid())).set(user);
        }
        return user;
    }

    static public UserModel updateUser(UserModel user) {
        ApiFuture<WriteResult> docRef = Application.firestore.collection("traders").document(String.valueOf(user.getUid())).set(user);

        return user;
    }

    static public UserModel getUser(String uid) throws InterruptedException, ExecutionException {
        DocumentSnapshot documentSnapshot = Application.firestore.collection("traders").document(uid).get().get();
        if (documentSnapshot.exists()) {
            UserModel user = documentSnapshot.toObject(UserModel.class);
            return user;
        }
        return null;
    }

    static public UserModel[] getAllUsers() throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> query = Application.firestore.collection("traders").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
//        List<UserModel> userModels = new LinkedList<>();
//        documents.forEach((doc) -> {
//            userModels.add(doc.toObject(UserModel.class));
//        });
        UserModel[] userarr = new UserModel[documents.size()];
        int i = 0;
        for (QueryDocumentSnapshot document : documents) {
            userarr[i] = document.toObject(UserModel.class);
            i++;
        }
        return userarr;
    }

    public static UserModel auth(String username, String password) throws InterruptedException, ExecutionException {
        List<QueryDocumentSnapshot> documentSnapshots = Application.firestore.collection("traders").whereEqualTo("username", username).whereEqualTo("password", password).get().get().getDocuments();
        if (!documentSnapshots.isEmpty()) {
            UserModel user = documentSnapshots.get(0).toObject(UserModel.class);
            return user;
        }
        return null;
    }

    public static UserModel[] findAll(String value) {
        ApiFuture<QuerySnapshot> query = Application.firestore.collection("traders").get();
        QuerySnapshot querySnapshot;
        //long startTime, endTime;

        try {
            querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

//            startTime = System.nanoTime();
//            List<UserModel> userModels = new LinkedList<>();
//            documents.forEach((doc) -> {
//                userModels.add(doc.toObject(UserModel.class));
//            });
//            endTime = System.nanoTime();
//            printExecutionTime(startTime, endTime);

            //startTime = System.nanoTime();
            UserModel[] userarr = new UserModel[documents.size()];
            int i = 0;
            for (QueryDocumentSnapshot document : documents) {
                userarr[i] = document.toObject(UserModel.class);
                i++;
            }
            //endTime = System.nanoTime();
            //printExecutionTime(startTime, endTime);
            return userarr;
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static void printExecutionTime(long startTime, long endTime) {
        long time_ns = endTime - startTime;
        long time_ms = TimeUnit.NANOSECONDS.toMillis(time_ns);
        long time_sec = TimeUnit.NANOSECONDS.toSeconds(time_ns);
        long time_min = TimeUnit.NANOSECONDS.toMinutes(time_ns);
        long time_hour = TimeUnit.NANOSECONDS.toHours(time_ns);

        System.out.print("\nExecution Time: ");
        if (time_hour > 0) {
            System.out.print(time_hour + " Hours, ");
        }
        if (time_min > 0) {
            System.out.print(time_min % 60 + " Minutes, ");
        }
        if (time_sec > 0) {
            System.out.print(time_sec % 60 + " Seconds, ");
        }
        if (time_ms > 0) {
            System.out.print(time_ms % 1E+3 + " MicroSeconds, ");
        }
        if (time_ns > 0) {
            System.out.print((time_ns % 1E+6) / 1000 + " NanoSeconds\n\n");
        }
    }
}
