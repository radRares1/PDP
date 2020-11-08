using System;

namespace lab4
{
    public class HTTPParser
    {
        public static int PORT = 80;

        public static string getResponseBody(string content)
        {
            var result = content.Split(new[] {"\r\n\r\n"}, StringSplitOptions.RemoveEmptyEntries);
            if (result.Length > 0)
            {
                return result[0];
            }

            return String.Empty;
        }

        public static string getRequest(string host, string endpoint)
        {
            Console.WriteLine("endpoint = " + endpoint);
            return "GET " + endpoint + " HTTP/1.1\r\n" +
                   "Host: " + host + "\r\n" +
                   "AllowAutoRedirect: true\r\n" +
                   "Content-Length: 0\r\n" + 
                    "\r\n";
        }

        public static int getContentLen(string respContent)
        {
            var contentLen = 0;
            var respLines = respContent.Split('\n', '\r');
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
        
        public static bool gotResponseHeader(string responseContent)
        {
            return responseContent.Contains("\r\n\r\n");
        }
    }
}