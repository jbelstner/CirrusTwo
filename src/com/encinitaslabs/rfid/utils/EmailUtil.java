/*
 * Copyright (c) 2013 - 2015, Encinitas Laboratories, Inc.
 * All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Encinitas Laboratories, Incorporated and its
 * suppliers if any.  The intellectual and technical concepts contained
 * herein are proprietary to Encinitas Laboratories, Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Encinitas Laboratories, Incorporated.
 *
 * Please contact:
 * Encinitas Laboratories, Inc.
 * 1310 Ravean Court
 * Encinitas, CA 92024 USA
 * http://www.encinitaslabs.com
 * for additional information or to ask a question.
 */
package com.encinitaslabs.rfid.utils;

import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;


public class EmailUtil {

    private static final Logger log = Logger.getLogger(EmailUtil.class);
    // TODO: Make these items configurable
    private String smtpHost = "smtp.office365.com";
    private String tlsPort = "587";
    private String sslPort = "465";
    private String fromAddress = "contact@encinitaslabs.com";
    private String password = "itsallabouttheE!";

    /**
     * EmailUtil
     *
     * Class Constructor
     *
     * @param log_ A Log object reference for logging activities.
     */
    public EmailUtil() {

    }

    /**
     * Utility method to send email without authentication
     *
     * @param toEmail
     * @param subject
     * @param body
     */
    public void sendNoAuth(String toAddress, String subject, String body, String attachment) {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);

        Session session = Session.getInstance(props, null);
        log.debug("Session created");
        if (attachment == null) {
            sendEmail(session, toAddress, subject, body);
        } else {
            sendAttachmentEmail(session, toAddress, subject, body, attachment);
        }
    }

    /**
     * Utility method to send TLS authenticated email
     *
     * @param toEmail
     * @param subject
     * @param body
     */
    public void sendTLS(String toAddress, String subject, String body, String attachment) {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", tlsPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            // override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromAddress, password);
            }
        };

        Session session = Session.getInstance(props, auth);
        if (session == null) {
        	log.warn("Could not get an instance of a TLS session");
        	return;
        }
        
        log.info("TLS session created");
        if (attachment == null) {
            sendEmail(session, toAddress, subject, body);
        } else {
            sendAttachmentEmail(session, toAddress, subject, body, attachment);
        }
    }

    /**
     * Utility method to send SSL authenticated email
     *
     * @param toEmail
     * @param subject
     * @param body
     */
    public void sendSSL(String toAddress, String subject, String body, String attachment) {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", sslPort);
        props.put("mail.smtp.socketFactory.port", sslPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");

        // create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            // override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromAddress, password);
            }
        };

        Session session = Session.getDefaultInstance(props, auth);
        if (session == null) {
        	log.warn("Could not get an instance of an SSL session");
        	return;
        }
        
        log.debug("SSL session created");
        if (attachment == null) {
            sendEmail(session, toAddress, subject, body);
        } else {
            sendAttachmentEmail(session, toAddress, subject, body, attachment);
        }
    }

    /**
     * Utility method to send HTML email
     *
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    private void sendEmail(Session session, String toAddress, String subject, String body) {
        MimeMessage msg = new MimeMessage(session);
        // set message headers
        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(fromAddress, "NoReply-JD"));
            msg.setReplyTo(InternetAddress.parse(fromAddress, false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress, false));
            Transport.send(msg);
            log.debug("Email sent successfully!");

        } catch (SendFailedException e) {
            log.error("sendEmail SendFailedException " + e.toString());
        } catch (MessagingException e) {
            log.error("sendEmail MessagingException " + e.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("sendEmail UnsupportedEncodingException " + e.toString());
        }
    }

    /**
     * Utility method to send an email with a file attachment
     *
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    public void sendAttachmentEmail(Session session, String toAddress, String subject, String body, String filename) {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(fromAddress, "NoReply-JD"));
            msg.setReplyTo(InternetAddress.parse(fromAddress, false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress, false));

            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(body);

            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Second part is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            msg.setContent(multipart);
            Transport.send(msg);
            log.debug("Email with attachment sent successfully!");

        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Utility method to send an image in the email body
     *
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    public void sendImageEmail(Session session, String toAddress, String subject, String body, String filename) {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(fromAddress, "NoReply-JD"));
            msg.setReplyTo(InternetAddress.parse(fromAddress, false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress, false));

            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Second part is image attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);

            // Trick is to add the content-id header here
            messageBodyPart.setHeader("Content-ID", "image_id");
            multipart.addBodyPart(messageBodyPart);

            // third part for displaying image in the email body
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("<h1>Attached Image</h1>" + "<img src='cid:image_id'>", "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Set the multipart message to the email message
            msg.setContent(multipart);
            Transport.send(msg);
            log.debug("Email with image sent successfully!");

        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
