import urllib.request
import ssl

ssl_context = ssl._create_unverified_context()

extensions = [".jpg", ".png", ".jpeg", ".webp"]
slugs = ["karakasvet", "shadowghast", "karakasvet-shadowghast"]
isbns = ["9786050842937", "9786050842920"]

urls = []
for slug in slugs:
    for isbn in isbns:
        for ext in extensions:
            urls.append(f"https://cdn.timas.com.tr/urun/{slug}-{isbn}{ext}")
            urls.append(f"https://cdn.timas.com.tr/urun/medium_{slug}-{isbn}{ext}")

print(f"Testing {len(urls)} URLs...")
for url in urls:
    try:
        req = urllib.request.Request(
            url, 
            headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
        )
        with urllib.request.urlopen(req, timeout=3, context=ssl_context) as response:
            if response.status == 200:
                print(f"[OK] {url}")
    except Exception:
        pass
print("Done testing.")
