import urllib.request
import ssl

ssl_context = ssl._create_unverified_context()
url = "https://cdn.timas.com.tr/urun/gargantis-9786050838039.jpg"

try:
    req = urllib.request.Request(
        url,
        headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
    )
    with urllib.request.urlopen(req, timeout=5, context=ssl_context) as response:
        print("Status:", response.status)
        print("URL is valid!")
except Exception as e:
    print("Error:", e)
