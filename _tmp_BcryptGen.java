import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class _tmp_BcryptGen {
  public static void main(String[] args) {
    System.out.print(new BCryptPasswordEncoder().encode("123456"));
  }
}