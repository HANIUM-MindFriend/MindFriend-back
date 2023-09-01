import torch
from xgb_model import BERTClassifier  # 실제 모델 파일을 가져옵니다

# 모델 로드
model = BERTClassifier(bert, num_classes, dr_rate)
model.load_state_dict(torch.load("/Users/songjuhee/Downloads/xgb_model.py"))
model.eval()

# 예측 함수 정의
def perform_inference(sentence):
    # 입력 데이터 전처리
    # 모델을 훈련할 때 사용한 전처리 단계와 일치하도록 입력 문장을 전처리해야 합니다.
    # 토큰화, 인코딩 및 필요한 기타 변환 단계가 포함될 수 있습니다.
    # 예시 (이 부분은 사용하는 전처리 단계에 맞게 수정하세요):
    token_ids, valid_length, segment_ids = preprocess_input(sentence)

    # 모델 예측
    # 이제 전처리된 입력 데이터로 모델 예측을 수행합니다.
    # 예시 (모델 아키텍처 및 입력 형식에 맞게 수정하세요):
    token_ids_tensor = torch.tensor([token_ids]).to(device)
    segment_ids_tensor = torch.tensor([segment_ids]).to(device)
    valid_length_tensor = torch.tensor([valid_length]).to(device)

    model.eval()  # 모델을 평가 모드로 설정합니다.
    with torch.no_grad():
        predictions = model(token_ids_tensor, valid_length_tensor, segment_ids_tensor)

    predicted_label = torch.argmax(predictions).item()  # 예측된 클래스 레이블을 가져옵니다.

    # 예측된 레이블을 반환합니다 (이 부분은 레이블 매핑에 맞게 수정하세요)
    return predicted_label


# 사용자 입력 받아 예측 실행
sentence = input("문장을 입력하세요: ")
predicted_label = perform_inference(sentence)
print(predicted_label)
