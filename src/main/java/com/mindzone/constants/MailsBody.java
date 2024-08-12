package com.mindzone.constants;


import com.mindzone.dto.MailDTO;

import static com.mindzone.constants.Constants.MINDZONE_HOMEPAGE;

public class MailsBody {

    public static MailDTO therapyRequestMail(
            String professionalMail,
            String patientName
    ) {
        return new MailDTO(
                professionalMail,
                patientName + " requested you for therapy",
                patientName + " wants you to be his/her therapist! Go to "
                        + MINDZONE_HOMEPAGE + " and analyse its request."
        );
    }

}
