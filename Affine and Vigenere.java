<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Affine & Vigenere Ciphers</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background: #f0f2f5; display: flex; justify-content: center; padding: 20px; }
        .container { background: white; padding: 2rem; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); width: 100%; max-width: 500px; }
        h2 { text-align: center; color: #333; margin-bottom: 20px; }
        textarea { width: 100%; height: 80px; padding: 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 1rem; box-sizing: border-box; resize: none; margin-bottom: 20px; }
        .controls { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; }
        .card { border: 1px solid #eee; padding: 15px; border-radius: 10px; background: #fafafa; text-align: center; }
        h3 { margin-top: 0; font-size: 1.1rem; color: #555; }
        input { width: 90%; margin: 8px 0; padding: 8px; border: 1px solid #ccc; border-radius: 4px; font-size: 0.9rem; }
        .btn-group { display: flex; gap: 5px; justify-content: center; margin-top: 10px; }
        button { flex: 1; border: none; padding: 10px; cursor: pointer; border-radius: 6px; font-weight: 600; font-size: 0.85rem; transition: 0.2s; }
        .btn-enc { background: #007bff; color: white; }
        .btn-dec { background: #6c757d; color: white; }
        button:hover { opacity: 0.85; }
        .result-box { margin-top: 25px; padding: 15px; background: #eef7ff; border-left: 5px solid #007bff; border-radius: 4px; }
        #resultText { margin: 10px 0 0; font-family: monospace; font-size: 1.1rem; word-wrap: break-word; color: #0056b3; }
    </style>
</head>
<body>

<div class="container">
    <h2>Cipher Tool</h2>
    <textarea id="inputText" placeholder="Type your message here..."></textarea>

    <div class="controls">
        <!-- Affine Card -->
        <div class="card">
            <h3>Affine</h3>
            <input type="number" id="affineA" value="5" placeholder="Key a (e.g. 5)">
            <input type="number" id="affineB" value="8" placeholder="Key b (e.g. 8)">
            <div class="btn-group">
                <button class="btn-enc" onclick="runAffine('enc')">Encrypt</button>
                <button class="btn-dec" onclick="runAffine('dec')">Decrypt</button>
            </div>
        </div>

        <!-- Vigenere Card -->
        <div class="card">
            <h3>Vigenère</h3>
            <input type="text" id="vigenereKey" placeholder="Enter keyword">
            <div class="btn-group">
                <button class="btn-enc" onclick="runVigenere('enc')">Encrypt</button>
                <button class="btn-dec" onclick="runVigenere('dec')">Decrypt</button>
            </div>
        </div>
    </div>

    <div class="result-box">
        <strong>Result:</strong>
        <p id="resultText">Output will appear here...</p>
    </div>
</div>

<script>
// --- CORE LOGIC ---

// Affine Cipher helper: find modular inverse of a mod 26
function modInverse(a, m) {
    for (let x = 1; x < m; x++) {
        if ((a * x) % m === 1) return x;
    }
    return null;
}

// Affine Process
function runAffine(mode) {
    const text = document.getElementById("inputText").value.toUpperCase();
    const a = parseInt(document.getElementById("affineA").value);
    const b = parseInt(document.getElementById("affineB").value);
    let result = "";

    if (mode === 'enc') {
        result = text.replace(/[A-Z]/g, (c) => {
            let x = c.charCodeAt(0) - 65;
            return String.fromCharCode(((a * x + b) % 26) + 65);
        });
    } else {
        const aInv = modInverse(a, 26);
        if (aInv === null) {
            result = "Error: 'a' must be coprime with 26 (e.g., 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25).";
        } else {
            result = text.replace(/[A-Z]/g, (c) => {
                let y = c.charCodeAt(0) - 65;
                let res = (aInv * (y - b)) % 26;
                if (res < 0) res += 26;
                return String.fromCharCode(res + 65);
            });
        }
    }
    document.getElementById("resultText").innerText = result || "Empty Input";
}

// Vigenere Process
function runVigenere(mode) {
    const text = document.getElementById("inputText").value.toUpperCase();
    const key = document.getElementById("vigenereKey").value.toUpperCase().replace(/[^A-Z]/g, '');
    if (!key) { alert("Please enter a keyword for Vigenère!"); return; }

    let result = "";
    let keyIdx = 0;
    const isDecrypt = (mode === 'dec');

    for (let i = 0; i < text.length; i++) {
        let char = text[i];
        if (char.match(/[A-Z]/)) {
            let p = char.charCodeAt(0) - 65;
            let k = key[keyIdx % key.length].charCodeAt(0) - 65;
            let shift = isDecrypt ? (p - k + 26) % 26 : (p + k) % 26;
            result += String.fromCharCode(shift + 65);
            keyIdx++;
        } else {
            result += char; // Spaces/punctuation stay the same
        }
    }
    document.getElementById("resultText").innerText = result || "Empty Input";
}
</script>

</body>
</html>
