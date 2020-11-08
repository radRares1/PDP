﻿using System;

namespace Lab5
{
    public class HttpParser
    {
        public static int PORT = 80; // http default port

        public static string GetRespBody(string responseContent)
        {
            // split response by removing new and empty lines
            var result = responseContent.Split(new[] {"\r\n\r\n"}, StringSplitOptions.RemoveEmptyEntries);
            if (result.Length > 1)
            {
                return result[1];
            }
            return "";
        }

        /// <summary>
        /// get request string
        /// </summary>
        /// <param name="hostname"> website </param>
        /// <param name="endpoint"> website nav specifics (after '/')</param>
        /// <returns> request string </returns>
        public static string GetRequestString(string hostname, string endpoint)
        {
            return "GET " + endpoint + " HTTP/1.1\r\n" +
                   "Host: " + hostname + "\r\n" +
                   "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36\r\n" +
                   "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,#1#*;q=0.8\r\n" +
                   "Accept-Language: en-US,en;q=0.9,ro;q=0.8\r\n" +
                   // the server will add the content-length header ONLY if the data comes archived (gzip)
                   "Accept-Encoding: gzip, deflate\r\n" +
                   "Connection: keep-alive\r\n" +
                   "Upgrade-Insecure-Requests: 1\r\n" +
                   "Pragma: no-cache\r\n" +
                   "Cache-Control: no-cache\r\n" +
                   "Content-Length: 0\r\n\r\n";
        }

        /// <summary>
        /// get content length of response body
        /// </summary>
        /// <param name="respContent"> response message from server </param> 
        /// <returns></returns>
        public static int GetContentLen(string respContent)
        {
            var contentLen = 0;
            var respLines = respContent.Split('\r', '\n');
            foreach (string respLine in respLines)
            {
                var headDetails = respLine.Split(':');

                if (String.Compare(headDetails[0], "Content-Length", StringComparison.Ordinal) == 0)
                {
                    contentLen = int.Parse(headDetails[1]);
                }
            }

            return contentLen;
        }

        /// <summary>
        /// function to check if a header was obtained in the response
        /// </summary>
        /// <param name="responseContent"> response body from server </param>
        /// <returns></returns>
        public static bool ResponseHeaderObtained(string responseContent)
        {
            return responseContent.Contains("\r\n\r\n");
        }
    }
}