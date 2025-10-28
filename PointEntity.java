package com.example.loyaltyprogram.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PointEntity
 * ユーザーのポイント情報を管理するデータモデル。
 */
@Entity
@Table(name = "points")
public class PointEntity {

    // --- フィールド定義 ---

    /**
     * ポイント識別子（主キー）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ユーザーID（外部キー）
     * UserEntityとの関連付け
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    /**
     * 現在のポイント残高
     */
    @Column(name = "balance", nullable = false)
    private int balance;

    /**
     * ポイント獲得・消費の履歴
     * PointHistoryEntityとの関連付け
     */
    @OneToMany(mappedBy = "pointEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PointHistoryEntity> history;

    /**
     * 初めてポイントアカウントが作成された日時
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * ポイント残高が最後に更新された日時
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- コンストラクタ ---

    public PointEntity() {
    }

    public PointEntity(UserEntity user, int balance) {
        this.user = user;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // --- Getter & Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
        this.updatedAt = LocalDateTime.now(); // 残高更新時に最終更新日時を更新する
    }

    public List<PointHistoryEntity> getHistory() {
        return history;
    }

    public void setHistory(List<PointHistoryEntity> history) {
        this.history = history;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // --- ヘルパーメソッド ---

    /**
     * 保持しているポイント履歴を追加する。
     * 
     * @param historyEntry 新規ポイント履歴エントリ
     */
    public void addHistory(PointHistoryEntity historyEntry) {
        this.history.add(historyEntry);
    }
}