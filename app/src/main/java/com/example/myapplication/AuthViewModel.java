package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AuthViewModel extends ViewModel {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<AuthState> _authState = new MutableLiveData<>(new AuthState.Idle());
    public LiveData<AuthState> authState = _authState;

    public void login(String email, String password) {
        _authState.setValue(new AuthState.Loading());
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _authState.setValue(new AuthState.Success());
                    } else {
                        _authState.setValue(new AuthState.Error(getDetailedError(task.getException())));
                    }
                });
    }

    public void signup(String name, String email, String password) {
        _authState.setValue(new AuthState.Loading());
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveUserToFirestore(name, email);
                    } else {
                        _authState.setValue(new AuthState.Error(getDetailedError(task.getException())));
                    }
                });
    }

    public void resetPassword(String email) {
        if (email == null || email.isEmpty()) {
            _authState.setValue(new AuthState.Error("Please enter your email to reset password"));
            return;
        }
        _authState.setValue(new AuthState.Loading());
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _authState.setValue(new AuthState.Message("Password reset email sent to " + email));
                    } else {
                        _authState.setValue(new AuthState.Error(getDetailedError(task.getException())));
                    }
                });
    }

    private void saveUserToFirestore(String name, String email) {
        if (mAuth.getCurrentUser() == null) return;
        
        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("uid", userId);
        user.put("name", name);
        user.put("email", email);
        user.put("role", "farmer");

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> _authState.setValue(new AuthState.Success()))
                .addOnFailureListener(e -> _authState.setValue(new AuthState.Error("Account created, but database profile failed: " + e.getMessage())));
    }

    private String getDetailedError(Exception e) {
        if (e instanceof FirebaseAuthException) {
            String code = ((FirebaseAuthException) e).getErrorCode();
            switch (code) {
                case "ERROR_WRONG_PASSWORD": return "Incorrect password. Try again.";
                case "ERROR_INVALID_CREDENTIAL": return "Invalid email or password.";
                case "ERROR_USER_NOT_FOUND": return "No account found with this email.";
                case "ERROR_OPERATION_NOT_ALLOWED": return "Email/Password login is disabled.";
                case "ERROR_INVALID_EMAIL": return "The email format is invalid.";
                case "ERROR_WEAK_PASSWORD": return "Password must be at least 6 characters.";
            }
        }
        return e != null ? e.getMessage() : "Authentication failed";
    }

    public static abstract class AuthState {
        public static class Idle extends AuthState {}
        public static class Loading extends AuthState {}
        public static class Success extends AuthState {}
        public static class Message extends AuthState {
            public final String msg;
            public Message(String m) { this.msg = m; }
        }
        public static class Error extends AuthState {
            public final String message;
            public Error(String m) { this.message = m; }
        }
    }
}
