import urllib.request
import ssl

ssl_context = ssl._create_unverified_context()
url = "https://vertexaisearch.cloud.google.com/grounding-api-redirect/AUZIYQGu76moTl0gE1hloG3wY3kWWxdu7PSjMfeIIPdj_XMnaL-uZaDqUIvHKaBKaJQkx2674lpfmRZq4nqb9KaiHL2Zr_lD7PIbW4Th6aoHoe15"

try:
    req = urllib.request.Request(
        url,
        headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
    )
    with urllib.request.urlopen(req, timeout=10, context=ssl_context) as response:
        print("Redirected URL:", response.geturl())
except Exception as e:
    print("Error:", e)
