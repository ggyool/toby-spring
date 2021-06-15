package org.ggyool.toby.user.exception;

public class DuplicateUserIdException extends RuntimeException {

    public DuplicateUserIdException(String userId) {
        super(String.format("유저ID가 중복됐습니다. (ID: %s)", userId));
    }
}
