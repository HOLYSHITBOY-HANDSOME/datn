package board.game.dao;

import board.game.entity.User;
import java.util.List;

/**
 *
 * @author LAPTOP LE SON
 */
public interface UserDAO extends CrudDAO<User, String> {

    void updatePassword(String username, String newPassword);

    String generateNewUserId();

    boolean isUsernameExists(String username);

    void updateTitle(String userId, String newTitle); // Tùy chọn, nếu bạn cần dùng riêng

}
