package it.unitn.disi.wp.cup.util;

import it.unitn.disi.wp.cup.persistence.entity.PersonAvatar;
import it.unitn.disi.wp.cup.persistence.entity.PersonSex;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Util class for managing {@link PersonAvatar personAvatar} easily
 *
 * @author Carlo Corradini
 */
public final class PersonAvatarUtil {

    /**
     * Check if the {@code personAvatar} is null, if true assign a default {@link PersonAvatar avatar} based on {@code personSex}
     *
     * @param personAvatar The {@link PersonAvatar personAvatar} to check
     * @param personId     The id of the {@link Person person} to check
     * @param personSex    The sex of the {@link Person person} to check
     * @return A new {@link PersonAvatar avatar} if it's null, the previous one otherwise
     */
    public static PersonAvatar checkPersonAvatar(PersonAvatar personAvatar, Long personId, PersonSex personSex) {
        if (personAvatar == null) {
            personAvatar = new PersonAvatar();
            personAvatar.setId(PersonAvatar.NOT_A_VALID_ID);
            personAvatar.setPersonId(personId);
            personAvatar.setUpload(LocalDateTime.now());
            personAvatar.setName(PersonAvatar.DEFAULT_AVATARS.get(personSex.getSex()));
        }

        return personAvatar;
    }
}
