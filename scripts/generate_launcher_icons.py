#!/usr/bin/env python3
"""Generate legacy launcher PNGs (mdpi..xxxhdpi) for the Laper! bowl glyph.

Renders the same design as ic_launcher_foreground.xml at high resolution,
then downscales. Run from anywhere; writes into app/src/main/res/mipmap-*.
"""
from pathlib import Path
from PIL import Image, ImageDraw

RES = Path(__file__).resolve().parent.parent / "app" / "src" / "main" / "res"
SS = 8  # supersampling factor
BASE = 512 * SS

CREAM = (255, 248, 245, 255)
BROWN = (154, 52, 18, 255)
ORANGE = (194, 65, 12, 255)
AMBER = (245, 158, 11, 255)


def render_rounded(size: int, radius_ratio: float) -> Image.Image:
    img = Image.new("RGBA", (BASE, BASE), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    radius = int(BASE * radius_ratio)
    d.rounded_rectangle([0, 0, BASE - 1, BASE - 1], radius=radius, fill=CREAM)
    draw_bowl(d)
    return img.resize((size, size), Image.LANCZOS)


def render_circle(size: int) -> Image.Image:
    img = Image.new("RGBA", (BASE, BASE), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    d.ellipse([0, 0, BASE - 1, BASE - 1], fill=CREAM)
    draw_bowl(d)
    return img.resize((size, size), Image.LANCZOS)


def draw_bowl(d: ImageDraw.ImageDraw) -> None:
    # Coordinates on the 108dp design grid, centered bowl like the vector.
    def s(v: float) -> float:
        return v / 108.0 * BASE

    line = int(s(3.5))
    # Steam: two curves (approximated with short arcs)
    for cx in (46, 62):
        pts = []
        for t in range(0, 101):
            # cubic-ish wiggle
            y = s(30) + t / 100.0 * (s(41) - s(30))
            x = s(cx) + ((t % 100) / 100.0 - 0.5) * s(4) * (1 if t < 50 else -1)
            pts.append((x, y))
        d.line(pts, fill=AMBER, width=line, joint="curve")

    # Bowl body: bottom half-ellipse
    d.pieslice(
        [s(30), s(55) - s(21), s(78), s(55) + s(21)],
        start=0, end=180, fill=ORANGE,
    )
    # Rim
    d.rounded_rectangle(
        [s(27), s(49), s(81), s(56)], radius=int(s(2.5)), fill=BROWN
    )
    # Foot
    d.rounded_rectangle(
        [s(46), s(73), s(62), s(79)], radius=int(s(2)), fill=BROWN
    )


DENSITIES = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192,
}

for folder, size in DENSITIES.items():
    out = RES / folder
    out.mkdir(parents=True, exist_ok=True)
    render_rounded(size, 0.22).save(out / "ic_launcher.png")
    render_circle(size).save(out / "ic_launcher_round.png")
    print(f"{folder}/ic_launcher.png + ic_launcher_round.png ({size}px)")

print("done")
