import io
import os
import json
from flask import Flask, request, jsonify
from PIL import Image
import google.generativeai as genai
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()

app = Flask(__name__)

# Initialize Gemini API
GEMINI_API_KEY = os.getenv('GEMINI_API_KEY')
if not GEMINI_API_KEY:
    raise ValueError("GEMINI_API_KEY environment variable is not set. Please set it in .env or your environment.")

genai.configure(api_key=GEMINI_API_KEY)
model = genai.GenerativeModel('gemini-2.0-flash')

def analyze_screenshot_with_gemini(image_data: bytes) -> dict:
    """
    Analyzes a screenshot image using Gemini's vision capabilities.
    Classifies content type, infers user intent, and suggests an action.
    
    Returns a structured response matching AIResponse model:
    {
        "content_type": "screenshot|text|other",
        "intent": "reminder|calendar_event|note|delete|unknown",
        "action": "add_to_calendar|set_reminder|save_as_note|delete|none",
        "action_details": {
            "title": optional string,
            "datetime": optional ISO-8601 string,
            "note": optional string
        }
    }
    """
    try:
        # Convert binary image data to PIL Image
        image = Image.open(io.BytesIO(image_data)).convert('RGB')
        
        # Create the prompt for Gemini
        prompt = """Analyze this screenshot or image and help the user understand why they likely saved it.

Please respond with a JSON object in this exact format:
{
    "content_type": "screenshot" | "text" | "other",
    "intent": "reminder" | "calendar_event" | "note" | "delete" | "unknown",
    "action": "add_to_calendar" | "set_reminder" | "save_as_note" | "delete" | "none",
    "action_details": {
        "title": string or null,
        "datetime": ISO 8601 datetime string or null,
        "note": string or null
    }
}

Guidelines:
1. Classify the content type: Is this a screenshot, text document, or other image?
2. Infer intent: Why did the user likely save this? (reminder, calendar event, note, delete, or unknown)
3. Suggest action: What's the most helpful next step?
4. For calendar_event action: Extract any date/time and pre-fill title and datetime in ISO 8601 format
5. For reminders: Suggest a title and optional time
6. For notes: Brief summary in the note field
7. If unsure: Use "unknown" intent and "none" action

Analyze the image and return ONLY valid JSON, no additional text."""
        
        # Call Gemini API with the image
        response = model.generate_content([prompt, image])
        response_text = response.text.strip()
        
        # Parse JSON response from Gemini
        # Try to extract JSON if Gemini wraps it in markdown code blocks
        if response_text.startswith("```json"):
            response_text = response_text[7:]  # Remove ```json
        if response_text.startswith("```"):
            response_text = response_text[3:]  # Remove ```
        if response_text.endswith("```"):
            response_text = response_text[:-3]  # Remove trailing ```
        
        response_json = json.loads(response_text.strip())
        return response_json
        
    except json.JSONDecodeError as e:
        print(f"Failed to parse Gemini response as JSON: {e}")
        # Return safe default on parse error
        return {
            "content_type": "other",
            "intent": "unknown",
            "action": "none",
            "action_details": {
                "title": None,
                "datetime": None,
                "note": "Could not analyze image"
            }
        }
    except Exception as e:
        print(f"Error analyzing image with Gemini: {e}")
        # Return safe default on any error
        return {
            "content_type": "other",
            "intent": "unknown",
            "action": "none",
            "action_details": {
                "title": None,
                "datetime": None,
                "note": f"Error: {str(e)}"
            }
        }

@app.route('/get-response', methods=['POST'])
def get_response():
    """
    Handles POST request with image data.
    Analyzes the image using Gemini and returns screenshot intent analysis.
    """
    if 'image' not in request.files:
        image_data = request.data
        if not image_data:
            return jsonify({'error': 'No image data found in request'}), 400
    else:
        image_data = request.files['image'].read()
    
    try:
        result = analyze_screenshot_with_gemini(image_data)
        return jsonify(result)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)