import io
from flask import Flask, request, jsonify
import tensorflow as tf
from PIL import Image
import numpy as np

app = Flask(__name__)

# Load the pre-trained MobileNetV2 model
model = tf.keras.applications.MobileNetV2(weights='imagenet')

def prepare_image(image_data):
    """
    Decodes image data, resizes it to the required input size for MobileNetV2,
    and preprocesses it for the model.
    """
    image = Image.open(io.BytesIO(image_data)).convert('RGB')
    image = image.resize((224, 224))
    image = np.array(image)
    image = tf.keras.applications.mobilenet_v2.preprocess_input(image)
    image = np.expand_dims(image, axis=0)
    return image

@app.route('/get-response', methods=['POST'])
def get_response():
    """
    Handles the POST request, receives image data,
    and returns the top prediction from the MobileNetV2 model.
    """
    if 'image' not in request.files:
        image_data = request.data
        if not image_data:
            return jsonify({'error': 'No image data found in request'}), 400
    else:
        image_data = request.files['image'].read()


    try:
        image = prepare_image(image_data)
        predictions = model.predict(image)
        decoded_predictions = tf.keras.applications.mobilenet_v2.decode_predictions(predictions, top=1)[0]
        top_prediction = decoded_predictions[0]
        _, label, score = top_prediction
        response = {
            'label': label,
            'score': float(score)
        }
        return jsonify(response)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)