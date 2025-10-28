package com.example.loyaltyprogram.service;

import com.example.loyaltyprogram.model.Benefit;
import com.example.loyaltyprogram.model.PointHistory;
import com.example.loyaltyprogram.model.Stage;
import com.example.loyaltyprogram.repository.BenefitRepository;
import com.example.loyaltyprogram.repository.PointHistoryRepository;
import com.example.loyaltyprogram.repository.StageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * LoyaltyService
 * ロイヤルティプログラムのビジネスロジックを提供するサービスクラス。
 * 主にポイント、特典、ステージ管理に関連する処理を実装します。
 */
@Service
public class LoyaltyService {

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private BenefitRepository benefitRepository;

    /**
     * ユーザーのポイント残高を取得。
     *
     * @param userId ユーザーID
     * @return 現在のポイント残高
     */
    public int getAvailablePoints(String userId) {
        return pointHistoryRepository.calculateTotalPoints(userId);
    }

    /**
     * 購入金額に基づいてポイントを付与します。
     *
     * @param userId  ユーザーID
     * @param amount  購入金額
     * @return 付与されたポイント数
     */
    public int addPoints(String userId, double amount) {
        int pointsEarned = (int) (amount * 0.1); // 購入金額の10%をポイントとして付与
        PointHistory pointHistory = new PointHistory(userId, pointsEarned, LocalDate.now(), "購入によるポイント付与");
        pointHistoryRepository.save(pointHistory);
        return pointsEarned;
    }

    /**
     * ポイントを利用して割引を適用。
     *
     * @param userId ユーザーID
     * @param points 使用するポイント数
     * @return ポイント利用成功ならtrue、失敗ならfalse（ポイント残高不足など）
     */
    public boolean usePoints(String userId, int points) {
        int availablePoints = getAvailablePoints(userId);
        if (availablePoints < points) {
            return false; // ポイント残高不足
        }
        PointHistory pointUsage = new PointHistory(userId, -points, LocalDate.now(), "ポイント使用");
        pointHistoryRepository.save(pointUsage);
        return true; // 正常にポイントが利用された
    }

    /**
     * ユーザーのポイント履歴を取得。
     *
     * @param userId ユーザーID
     * @return ポイント履歴のリスト
     */
    public List<PointHistory> getPointHistory(String userId) {
        return pointHistoryRepository.findByUserIdOrderByDateDesc(userId);
    }

    /**
     * ユーザーの現在のステージ情報を取得。
     *
     * @param userId ユーザーID
     * @return 現在のステージ情報
     */
    public Stage getStageDetails(String userId) {
        return stageRepository.findByUserId(userId);
    }

    /**
     * ユーザーのステージを更新（アップまたはダウン）。
     *
     * @param userId ユーザーID
     * @return 更新結果（新しいステージ名）
     */
    public String updateStage(String userId) {
        Stage currentStage = stageRepository.findByUserId(userId);
        int totalPoints = getAvailablePoints(userId);
        String newStage;
        
        if (totalPoints >= 1000) {
            newStage = "ゴールド会員";
        } else if (totalPoints >= 500) {
            newStage = "シルバー会員";
        } else {
            newStage = "ブロンズ会員";
        }
        
        if (!newStage.equals(currentStage.getName())) {
            currentStage.setName(newStage);
            stageRepository.save(currentStage);
        }
        
        return newStage;
    }

    /**
     * ステージごとの特典を取得。
     *
     * @return 特典のリスト
     */
    public List<Benefit> getAvailableBenefits() {
        return benefitRepository.findAll();
    }

    /**
     * 特定の特典を適用。
     *
     * @param userId    ユーザーID
     * @param benefitId 特典ID
     * @return 特典適用成功ならtrue、失敗ならfalse
     */
    public boolean applyBenefit(String userId, Long benefitId) {
        Benefit benefit = benefitRepository.findById(benefitId).orElse(null);
        if (benefit == null || !benefit.getEligibleStages().contains(stageRepository.findByUserId(userId).getName())) {
            return false; // 適用条件に合致しなかった
        }
        benefitRepository.markAsApplied(benefitId, userId);
        return true;
    }

    /**
     * ポイントの使用期限通知を送信。
     *
     * @param userId ユーザーID
     * @return 通知送信成功ならtrue、失敗ならfalse
     */
    public boolean notifyExpiration(String userId) {
        List<PointHistory> expiringPoints = pointHistoryRepository.findExpiringPoints(userId, LocalDate.now().plusDays(30));
        if (expiringPoints.isEmpty()) {
            return false; // 有効期限が近いポイントはない
        }
        // 外部通知サービスを通じてメールやSMSを送信する処理（仮実装）
        sendNotification(userId, "ポイントの有効期限が近づいています。確認してください。");
        return true;
    }

    /**
     * 外部通知サービスでメッセージをユーザーに送信（モックとして実装）。
     *
     * @param userId ユーザーID
     * @param message 通知内容
     */
    private void sendNotification(String userId, String message) {
        // 実際にはメールやSMS送信のロジックを実装
        System.out.println("通知送信: ユーザーID=" + userId + " 内容=" + message);
    }
}
