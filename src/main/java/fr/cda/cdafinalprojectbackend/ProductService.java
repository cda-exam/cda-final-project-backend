package fr.cda.cdafinalprojectbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    String _namme;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void unsafeQuery(String userInput) {
        String query = "SELECT * FROM users WHERE username = '" + userInput + "'"; // SQL Injection possible
        jdbcTemplate.execute(query);
    }
    public void testBug() {
        String str = null;
        System.out.println(str.length()); // NullPointerException potentiel (bug critique)
    }
}
