import urllib.request
import ssl

ssl_context = ssl._create_unverified_context()

urls = [
    "https://cdn.timas.com.tr/urun/festergrimm-9786050846379.jpg",
    "https://cdn.timas.com.tr/urun/festergrimm-9786050846669.jpg", # original
    "https://cdn.timas.com.tr/urun/shadowghast-9786050842937.jpg",
    "https://cdn.timas.com.tr/urun/shadowghast-9786050842920.jpg",
    "https://cdn.timas.com.tr/urun/karakasvet-9786050842937.jpg",
    "https://cdn.timas.com.tr/urun/karakasvet-9786050842920.jpg",
    "https://cdn.timas.com.tr/urun/mermedusa-9786050848274.jpg",
    "https://cdn.timas.com.tr/urun/mermedusa-9786050848267.jpg",
]

for url in urls:
    try:
        req = urllib.request.Request(
            url, 
            headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
        )
        with urllib.request.urlopen(req, timeout=5, context=ssl_context) as response:
            if response.status == 200:
                print(f"[OK] {url}")
            else:
                print(f"[ERR {response.status}] {url}")
    except Exception as e:
        print(f"[ERR] {url} -> {e}")
