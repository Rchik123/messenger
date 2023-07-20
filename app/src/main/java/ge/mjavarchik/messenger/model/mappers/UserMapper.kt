package ge.mjavarchik.messenger.model.mappers

import ge.mjavarchik.messenger.model.api.User
import ge.mjavarchik.messenger.model.data.UserEntity

class UserMapper {

    fun toEntity(user: User?, hashedPassword: String): UserEntity? {
        return user?.let {
            UserEntity(
                it.username,
                it.nickname,
                it.profession,
                hashedPassword
            )
        }
    }

    fun fromEntity(userEntity: UserEntity?): User? {
        return userEntity?.let {
            User(
                it.username,
                it.nickname,
                it.profession
            )
        }
    }
}