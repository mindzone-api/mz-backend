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

    public static MailDTO approvedTherapyRequestMail(
            String patientMail,
            String professionalName
    ) {
        return new MailDTO(
                patientMail,
                professionalName + " approved your therapy request!",
                professionalName + " has just approved your therapy request! Go to "
                        + MINDZONE_HOMEPAGE + " to analyse futher information."
        );
    }

    public static MailDTO deniedTherapyRequestMail(
            String patientMail,
            String professionalName,
            String denialJustification
    ) {
        return new MailDTO(
                patientMail,
                professionalName + " denied your therapy request",
                professionalName + " has denied your therapy request with the following " +
                        "justification:\n" + denialJustification
        );
    }

    public static MailDTO canceledTherapyRequestMail(
            String patientMail,
            String professionalName
    ) {
        return new MailDTO(
                patientMail,
                "your therapy request to " + professionalName + " was canceled",
                "Your therapy request to " + professionalName + " was automatically canceled due " +
                        "to this professional schedule conflict. Try again with its updated availability!"
        );
    }

    public static MailDTO therapyUpdateMail(
            String patientMail,
            String professionalName
    ) {
        return new MailDTO(
                patientMail,
                "your therapy with " + professionalName + "has been updated",
                professionalName + " has updated informations about your therapy, go to " + MINDZONE_HOMEPAGE +
                        "to see more details about it."
        );
    }

    public static MailDTO sessionsScheduleUpdateMail(
            String patientMail,
            String professionalName
    ) {
        return new MailDTO(
                patientMail,
                professionalName + " has updated your therapy schedule",
                professionalName + " has updated informations about your therapy schedule, go to " + MINDZONE_HOMEPAGE +
                        "to see more details about it."
        );
    }
}
