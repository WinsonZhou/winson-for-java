package com.winson.annotation;

/**
 * @author winson
 * @date 2021/6/15
 **/
public interface AnnotationService {

    @Timeout
    void addUser(@MaxLength(20) @NotEmpty String userName);

    void editUser(@NotEmpty @ValidClass(targetClass = ValidUser.class) AnnotationUser user);

    void addFriend(String country, int size, String... friends);

}
