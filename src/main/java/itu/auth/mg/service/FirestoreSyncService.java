package itu.auth.mg.service;

import com.google.cloud.firestore.Firestore;
import itu.auth.mg.model.User;
import itu.auth.mg.repositories.UserRepository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreSyncService {

    @Autowired
    private UserRepository userRep;

    @Autowired
    private Firestore firestore;

    public void syncUserToFirestore(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("nom", user.getNom());
        data.put("prenom", user.getPrenom());
        data.put("id", user.getId());
        data.put("password", user.getPassword());
    
        ApiFuture<DocumentReference> future = firestore.collection("users").add(data);
        try {
            DocumentReference docRef = future.get();
            String generatedId = docRef.getId();
            user.setUid(generatedId);
            userRep.save(user);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }    
}
