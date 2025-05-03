package org.lemine.emailvalidator.utils;

import com.sun.mail.smtp.SMTPTransport;
import jakarta.mail.Session;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import java.util.regex.Pattern;

public class EmailUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    /**
     *
     * @param email String
     * @return boolean
     */
    public static boolean isSyntaxValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * It checks whether the domain of an email address has valid MX (Mail Exchange) DNS records
     * @param email String
     * @return boolean
     */
    public static boolean hasValidMXRecord(String email) {
        String domain = StringUtils.substringAfter(email, "@");
        try {
            Lookup lookup = new Lookup(domain, Type.MX);
            org.xbill.DNS.Record[] records = lookup.run();
            return records != null && records.length > 0;
        } catch (TextParseException e) {
            return false;
        }
    }

    /**
     * To simulate an email delivery attempt at the SMTP (mail server) level — without actually sending the email —
     * and check if the recipient’s mailbox exists and accepts messages.
     * @param email String
     * @return boolean
     */
    public static boolean isSmtpValid(String email) {
        String domain = StringUtils.substringAfter(email, "@");
        try {
            Lookup lookup = new Lookup(domain, Type.MX);
            Record[] mxRecords = lookup.run();
            if (mxRecords == null || mxRecords.length == 0)
                return false;

            Arrays.sort(mxRecords, Comparator.comparingInt(r -> ((MXRecord) r).getPriority()));
            String mxHost = ((MXRecord) mxRecords[0]).getTarget().toString();

            Properties props = new Properties();
            props.put("mail.smtp.host", mxHost);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.timeout", "5000");
            props.put("mail.smtp.connectiontimeout", "5000");

            Session session = Session.getInstance(props);
            session.setDebug(false);

            SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
            transport.connect();

            transport.issueCommand("HELO yourdomain.com", 250);
            transport.issueCommand("MAIL FROM:<validate@yourdomain.com>", 250);
            int rcptResponse = transport.simpleCommand("RCPT TO:<" + email + ">");
            transport.close();

            return rcptResponse == 250 || rcptResponse == 251;
        } catch (Exception e) {
            return false;
        }
    }

}
