import urllib.request
import ssl

ssl_context = ssl._create_unverified_context()

variations = [
    "karakasvet",
    "shadowghast",
    "karakasvet-shadowghast",
    "shadowghast-karakasvet",
    "karakasvet-tuhaf-deniz-kasabasi-efsaneleri-3"
]

isbns = [
    "9786050842937",
    "9786050842920"
]

urls = []
for var in variations:
    urls.append(f"https://cdn.timas.com.tr/urun/{var}.jpg")
    for isbn in isbns:
        urls.append(f"https://cdn.timas.com.tr/urun/{var}-{isbn}.jpg")
        urls.append(f"https://cdn.timas.com.tr/urun/medium_{var}-{isbn}.jpg")
        urls.append(f"https://cdn.timas.com.tr/urun/small_{var}-{isbn}.jpg")

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
